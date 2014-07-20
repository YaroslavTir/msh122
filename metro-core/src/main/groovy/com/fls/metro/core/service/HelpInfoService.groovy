package com.fls.metro.core.service
import com.fls.metro.core.data.dao.HelpInfoDao
import com.fls.metro.core.data.domain.HelpInfo
import com.fls.metro.core.data.domain.ObjectType
import com.fls.metro.core.data.dto.content.Language
import com.fls.metro.core.data.dto.content.help.*
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
/**
 * User: APermyakov
 * Date: 21.05.14
 * Time: 14:10
 */
@Slf4j
@Service
class HelpInfoService {

    @Autowired
    protected HelpInfoDao helpInfoDao

    @Autowired
    private HierarchyDataRetriever hierarchyDataRetriever

    @Transactional
    void processAndSave(List<HelpInfo> helps, ObjectType objectType, Long objectId) {
        List<HelpInfo> helpsForUPdate = new ArrayList<HelpInfo>()
        int index=1;
        helps.each {
            if (it.objectType != objectType) {
                return
            }
            it.objectType = objectType
            it.objectId = objectId
            it.updateDate = new Date()
            it.index=index
            helpsForUPdate.add(it)
            index++;
        }
        helpInfoDao.saveNewHelpInfoList(helpsForUPdate, objectType, objectId)
    }

    List<HelpInfo> getTotal(ObjectType objectType, long id) {
        def result = new ArrayList<HelpInfo>();

        result.addAll(hierarchyDataRetriever.collectAllLevels(objectType, id) { ObjectType ot, oid, o ->
            helpInfoDao.findAll(ot, oid).reverse()
        })

        result.reverse();
    }

    private HelpMenuItem createMenuItem(HelpInfo i){
        if(i.helpInfoType==HelpMenuItemDataType.HTML){
            return(new HelpMenuItemHtml(i))
        }else{
            return(new HelpMenuItemMedia(i))
        }
    }

    Map<Language, HelpMenu> getInfoContent(ObjectType objectType, long id) {

        Map<Language, HelpMenu> result = new  HashMap<Language, HelpMenu>();

        List<HelpInfo> resultDB = hierarchyDataRetriever.collectAllLevels(objectType, id) { ObjectType ot, oid, o ->
            helpInfoDao.findAll(ot, oid).reverse()
        }.reverse();

        for (HelpInfo i : resultDB) {
            HelpMenu helpMenu = result.get(i.language)
            if (helpMenu) {
                helpMenu.items.add(createMenuItem(i))
                helpMenu.updateDate=ContentService.getMostRecentDate(helpMenu.updateDate, i.updateDate)
            } else {
                result.put(
                        i.language,
                        new HelpMenu(items: [createMenuItem(i)], updateDate : i.updateDate))
            }
        }




        result
    }

    void delete(ObjectType ownerType, Long ownerId){
        helpInfoDao.deleteFor(ownerType, ownerId)
    }


}
