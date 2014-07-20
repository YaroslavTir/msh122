package com.fls.metro.core.service

import com.fls.metro.core.data.dao.ImDao
import com.fls.metro.core.data.domain.Im
import com.fls.metro.core.data.domain.ObjectType
import com.fls.metro.core.data.dto.RemoveHierarchyObjectResult
import com.fls.metro.core.security.UserDetailsIm
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * User: NFadin
 * Date: 25.04.14
 * Time: 10:14
 */
@Slf4j
@Service
class ImService extends HierarchyObjectService<Im> implements UserDetailsService {

    @Autowired
    private ImDao imDao

    @Autowired
    private SettingsService settingsService

    @Autowired
    private ScheduleService scheduleService

    @Value('${common.imRole}')
    private String imRole

    @Value('${im.defaultPort}')
    private Integer imDefaultPort

    @Override
    @Transactional
    UserDetailsIm loadUserByUsername(String imName) throws UsernameNotFoundException {
        def im = getByImName(imName)
        if (!im) {
            log.info('Im {} not found', imName)
            throw new UsernameNotFoundException('im.not.found')
        }
        userDetails(im)
    }

    @Transactional
    UserDetailsIm register(String name, String ip) throws UsernameNotFoundException {
        log.info('Register client with name {} and ip {}', name, ip)
        Im im = getByImName(name)
        if (!im) {
            log.info('Im {} not found', name)
            throw new UsernameNotFoundException('im.not.found')
        }
        im.ip = ip
        imDao.update(im)
        userDetails(im)
    }

    @Transactional
    Im getByImName(String imName) {
        imDao.findImByName(imName)
    }

    String getImNameById(Long id) {
        Im im = imDao.find(id);
        if (im != null) {
            return im.imName;
        }
        return null;
    }

    @Transactional
    Im getByImNameWithHierarchyData(String imName) {
        get(getByImName(imName)?.id, true)
    }

    UserDetailsIm userDetails(Im im) {
        new UserDetailsIm(
                imName: im.imName,
                authorities: [new SimpleGrantedAuthority(imRole)] as Set
        )
    }

    @Transactional
    List<String> ips(List<String> imNames) {
        imDao.ips(imNames)
    }

    @Transactional
    List<String> imNamesList() {
        imDao.imNamesList()
    }

    @Transactional
    Im create(Im im) {
        log.info('Create im with name {}', im.name)
        if (!im.port) {
            im.port = imDefaultPort
        }
        im = imDao.create(im)
        log.info('Im with name {}. Id is {}', im.name, im.id)
        return im
    }

    @Transactional
    Im update(Im im) {
        log.info('Update im with id {}', im.id)
        im = setDataForUpdate(ObjectType.IM, im) { Im it, Im old ->
            if (it.latitude != old.latitude || it.longitude != old.longitude) {
                it.staticDataUpdateDate = new Date()
            }
            return it
        }
        imDao.update(im)
        log.info('Im with id {} was successfully updated', im.id)
        return im
    }

    @Transactional
    Im get(Long id) {
        log.info('Get im with id {}', id)
        get(ObjectType.IM, id, true) { Im im ->
            return im
        }
    }

    @Transactional
    Im get(Long id, boolean copySettings) {
        log.info('Get im with id {}', id)
        get(ObjectType.IM, id, copySettings) { Im im ->
            return im
        }
    }



    @Transactional
    String ip(String s) {
        ips([s])[0]
    }

    @Transactional
    RemoveHierarchyObjectResult delete(Long id) {
        log.info('Delete im with id {}', id)
        prepareDelete(ObjectType.IM, id)
        imDao.delete(id)
        log.info('Im with id {} was successfully deleted', id)
        RemoveHierarchyObjectResult.OK
    }

    @Transactional
    List<Im> nameStartsWith(String imName) {
        imDao.nameStartsWith(imName)
    }
}
