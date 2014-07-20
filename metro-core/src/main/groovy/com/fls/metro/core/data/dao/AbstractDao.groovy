package com.fls.metro.core.data.dao

import com.fls.metro.core.annotation.*
import com.fls.metro.core.data.filter.AbstractFilter
import com.fls.metro.core.data.filter.Order
import com.fls.metro.core.sql.SqlHolder
import com.google.common.base.CaseFormat
import groovy.sql.GroovyRowResult
import groovy.sql.Sql
import org.springframework.beans.factory.annotation.Autowired

import javax.sql.DataSource
import java.beans.Introspector
import java.beans.PropertyDescriptor
import java.lang.reflect.Field
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.sql.Array

/**
 * User: NFadin
 * Date: 16.04.14
 * Time: 11:54
 */
abstract class AbstractDao<T> implements Dao<T> {

    @Autowired
    private SqlHolder sqlHolder
    @Autowired
    DataSource dataSourceProxy

    private Class<T> domainClass
    protected String idFieldName
    protected String tableName
    protected String sequenceName
    protected Map<String, String> collectionFields
    protected Map<String, String> fieldColumnNames
    protected List<String> timeFields
    protected List<String> dateFields
    protected List<String> timestampFields
    protected List<String> ignoredFields
    protected List<Field> fields
    protected List<PropertyDescriptor> propertyDescriptors

    final String selectStatement
    final String deleteStatement
    final String createStatementP1
    final String createStatementP2 = ") values ("
    final String createStatementP3 = ")"
    final String updateStatement
    final String updateStatementWhere

    protected getSql() {
        sqlHolder.sql
    }

    public AbstractDao() {
        Type t = this.class.genericSuperclass;
        ParameterizedType pt = (ParameterizedType) t;
        domainClass = pt.actualTypeArguments[0] as Class<T>;
        fields = calculateFields()
        fieldColumnNames = calculateFieldColumnNames()
        propertyDescriptors = calculatePropertiesDescriptors()
        idFieldName = calculateIdFieldName()
        tableName = calculateTableName()
        sequenceName = calculateSequenceName()
        collectionFields = calculateCollectionFields()
        timeFields = calculateTimeFields()
        dateFields = calculateDateFields()
        timestampFields = calculateTimestampFields()
        ignoredFields = calculateIgnoredFields()
        selectStatement = "select * from $tableName "
        deleteStatement = "delete from $tableName where "
        createStatementP1 = "insert into $tableName ("
        updateStatement = "update $tableName set "
        updateStatementWhere = " where "
    }

    private def calculateFields() {
        def result = []
        def c = domainClass
        result.addAll(c.declaredFields)
        while ((c = c.superclass) != null) {
            result.addAll(c.declaredFields)
        }
        return result
    }

    private def calculatePropertiesDescriptors() {
        def result = []
        def c = domainClass
        result.addAll(Introspector.getBeanInfo(c).propertyDescriptors)
        return result
    }

    private def calculateFieldColumnNames() {
        def result = [:]
        fields.each {
            if (it.annotations*.annotationType().contains(Column)) {
                result.put(it.name, it.getAnnotation(Column).value())
            } else {
                result << [(it.name): CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, it.name)]
            }
        }
        result
    }

    private def calculateCollectionFields() {
        def result = [:]
        fields.each {
            if (it.annotations*.annotationType().contains(ArrayField)) {
                result.put(it.name, it.getAnnotation(ArrayField).value())
            }
        }
        result
    }

    private def calculateTimeFields() {
        def result = []
        fields.each {
            if (it.annotations*.annotationType().contains(TimeField)) {
                result.add(dbLikeFieldName(it.name))
            }
        }
        result
    }

    private def calculateDateFields() {
        def result = []
        fields.each {
            if (it.annotations*.annotationType().contains(DateField)) {
                result.add(dbLikeFieldName(it.name))
            }
        }
        result
    }

    private def calculateTimestampFields() {
        def result = []
        fields.each {
            if (it.annotations*.annotationType().contains(TimestampField)) {
                result.add(dbLikeFieldName(it.name))
            }
        }
        result
    }

    private def calculateIdFieldName() {
        calculateFieldName(fields.find {
            it.annotations*.annotationType().contains(Id)
        })
    }

    private def calculateTableName() {
        def tableName
        def tableAn = domainClass.getAnnotation(Table)
        if (tableAn) {
            tableName = tableAn.value()
        } else {
            tableName = domainClass.simpleName
        }
        return dbLikeTableName(tableName)
    }

    String calculateSequenceName() {
        def sequenceAn = domainClass.getAnnotation(Seq)
        sequenceAn ? sequenceAn.value() : (domainClass.simpleName + "_seq")
    }

    List<String> calculateIgnoredFields() {
        def res = []
        fields.each {
            if (it.annotations*.annotationType().contains(Ignore)) {
                res << it.name
            }
        }
        propertyDescriptors.each {
            if (it.readMethod.annotations*.annotationType().contains(Ignore)) {
                res << it.name
            }
        }
        res
    }

    private String calculateFieldName(Field field) {
        dbLikeFieldName(field.name)
    }

    def String dbLikeFieldName(String s) {
        fieldColumnNames[s] ?: CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, s)
    }

    def static String dbLikeTableName(String s) {
        CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, s)
    }

    def T toDomain(GroovyRowResult groovyRowResult) {
        if (!groovyRowResult) {
            return null
        }
        def result = domainClass.newInstance()
        def clazz = result.metaClass
        clazz.properties.each {
            if (it.name == 'class') return
            if (ignoredFields.contains(it.name)) return
            def o = groovyRowResult.getProperty(dbLikeFieldName(it.name))
            if (o instanceof Array) {
                clazz.setProperty(result, it.name, o.getArray())
            } else {
                clazz.setProperty(result, it.name, o)
            }

        }
        return result
    }

    def prepareValue(String fieldName, def p) {
        if (p instanceof Enum) {
            return p.name()
        }
        if (collectionFields[fieldName]) {
            return Sql.ARRAY(dataSourceProxy.connection.createArrayOf(collectionFields[fieldName], p))
        }
        if (timeFields.contains(fieldName)) {
            return Sql.TIME(p)
        }
        if (dateFields.contains(fieldName)) {
            return Sql.DATE(p)
        }
        if (timestampFields.contains(fieldName)) {
            return Sql.TIMESTAMP(p)
        }
        return p
    }

    protected execute(Map<String, ?> fieldValueMap, Closure cb) {
        execute(fieldValueMap, null, cb)
    }

    protected List fieldValueMapToParamsData(Map<String, ?> fieldValueMap) {
        def params = [:]
        def fieldToParam = [:]
        fieldValueMap.each { k, v ->
            params << [("${k}Param".toString()): prepareValue(k, v)]
            fieldToParam << [("${dbLikeFieldName(k)}".toString()): ":${k}Param".toString()]
        }
        return [params, fieldToParam]
    }

    protected execute(Map<String, ?> fieldValueMap, def idFieldValue, Closure cb) {
        def pramsData = fieldValueMapToParamsData(fieldValueMap)
        def params = pramsData[0]
        def fieldToParam = pramsData[1]
        if (!idFieldValue) {
            cb.call(fieldToParam, params)
        } else {
            def idFieldToParam = [:]
            params << [("${idFieldName}Param".toString()): prepareValue(idFieldName, idFieldValue)]
            idFieldToParam << [("${dbLikeFieldName(idFieldName)}".toString()): ":${idFieldName}Param".toString()]
            cb.call(fieldToParam, idFieldToParam, params)
        }
    }

    protected T executeSelect(Map<String, ?> fieldValueMap) {
        def map = [:]
        fieldValueMap?.entrySet()?.each {
            map << [(dbLikeFieldName(it.key as String)): it.value]
        }
        execute(map) { wr, p ->
            if (wr && p) {
                toDomain(sql.firstRow(selectStatement + " where " + toAndSeparatedFPString(wr), p as Map))
            } else {
                toDomain(sql.firstRow(selectStatement))
            }
        } as T
    }

    protected List<T> executeSelectRows(Map<String, ?> fieldValueMap) {
        executeSelectRows(fieldValueMap, null, null, null)
    }

    protected List<T> executeSelectRows(Map<String, ?> fieldValueMap, def limit, def offset, List<Order> orders) {
        execute(fieldValueMap) { wr, p ->
            def request = selectStatement + (p ? ' where ' : '') + toAndSeparatedFPString(wr)
            if (orders) {
                request += ' order by ' + orders.collect { order ->
                    def res = ''
                    if (order.field && fieldColumnNames.containsKey(order.field)) {
                        res += dbLikeFieldName(order.field) + ' '
                        if (order.direction && (order.direction == 'ASC' || order.direction == 'DESC')) {
                            res += order.direction
                        } else {
                            res += 'ASC'
                        }
                    }
                    res
                }.join(', ')
            }

            if (limit != null && offset != null) {
                request += ' limit :limitParam offset :offsetParam'
                p << ['limitParam': limit] << ['offsetParam': offset]
            }
            if (p) {
                sql.rows(request, p as Map).collect {
                    toDomain(it)
                }
            } else {
                sql.rows(request).collect {
                    toDomain(it)
                }
            }
        } as List<T>
    }

    protected Map collectFieldValueMapForFilter(AbstractFilter filter) {
        def fieldValueMap = [:]
        filter.properties.entrySet().each {
            if (['class', 'currentPage', 'pageSize', 'orders'].contains(it.key) || !it.value) {
                return
            }
            fieldValueMap << [(dbLikeFieldName(it.key as String)): it.value]
        }
        return fieldValueMap
    }

    protected List<T> executeSelectRowsWithFilter(AbstractFilter filter) {
        def offset = (filter.currentPage - 1) * filter.pageSize
        def limit = filter.pageSize
        def orders = filter.orders
        def fieldValueMap = collectFieldValueMapForFilter(filter)
        executeSelectRows(fieldValueMap, limit, offset, orders)
    }

    protected Long executeCountWithFilter(AbstractFilter filter) {
        def query = "select count(*) as cnt from $tableName".toString()
        def fieldValueMap = collectFieldValueMapForFilter(filter)
        if (fieldValueMap) {
            def paramsData = fieldValueMapToParamsData(fieldValueMap)
            return sql.firstRow(query + " where " + toAndSeparatedFPString(paramsData[1]), paramsData[0] as Map).cnt
        }
        return sql.firstRow(query).cnt
    }

    static String toAndSeparatedFPString(wr) {
        toSeparatedFPString(wr, ' and ')
    }

    static String toCommaSeparatedKVString(wr) {
        toSeparatedFPString(wr, ', ')
    }

    static String toSeparatedFPString(wr, String separator) {
        wr.collect { k, v ->
            "$k = $v"
        }.join(separator)
    }

    protected List<List> executeCreate(Map<String, ?> fieldValueMap) {
        execute(fieldValueMap) { Map f2p, p ->
            sql.executeInsert(createStatementP1 + "${f2p.keySet().join(', ')}" +
                    createStatementP2 + "${f2p.values().join(', ')}" +
                    createStatementP3, p as Map)

        } as List<List>
    }

    protected executeUpdate(Map<String, ?> fieldValueMap, def idFieldValue) {
        execute(fieldValueMap, idFieldValue) { Map f2p, Map idf2p, p ->
            sql.executeUpdate(updateStatement + toCommaSeparatedKVString(f2p) +
                    updateStatementWhere + toCommaSeparatedKVString(idf2p), p as Map)
        }
    }

    protected void executeDelete(Map<String, ?> fieldValueMap) {
        execute(fieldValueMap) { wr, p ->
            sql.execute(deleteStatement + toAndSeparatedFPString(wr), p as Map)
        }
    }

    @Override
    T find(Object id) {
        executeSelect([(idFieldName): id])
    }

    @Override
    T create(T t) {
        def fieldValueMap = [:]
        t.properties.entrySet().each {
            if (it.key == 'class') return
            if (ignoredFields.contains(it.key)) return
            if (it.key == idFieldName && !it.value) return
            fieldValueMap << [(dbLikeFieldName(it.key as String)): it.value]
        }
        def ids = executeCreate(fieldValueMap)
        if (ids) {
            t.metaClass.setProperty(t, idFieldName, ids[0][0])
        }
        return t
    }

    @Override
    def update(T t) {
        def fieldValueMap = [:]
        def idFieldValue = null
        t.properties.each { k, v ->
            if (k == 'class') return
            if (ignoredFields.contains(k)) return
            if (k == idFieldName) {
                idFieldValue = v
                return
            }
            fieldValueMap << [(dbLikeFieldName(k as String)): v]
        }
        executeUpdate(fieldValueMap, idFieldValue)
    }

    @Override
    def delete(Object id) {
        executeDelete([(idFieldName): id])
    }

    @Override
    Long count(AbstractFilter filter) {
        executeCountWithFilter filter
    }

    @Override
    List<T> filter(AbstractFilter filter) {
        executeSelectRowsWithFilter filter
    }
}
