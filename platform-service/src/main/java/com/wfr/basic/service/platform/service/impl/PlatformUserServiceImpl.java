package com.wfr.basic.service.platform.service.impl;

import com.wfr.basic.service.platform.dao.service.IPlatformUserDaoService;
import com.wfr.basic.service.platform.model.dto.user.DeletePlatformUserDto;
import com.wfr.basic.service.platform.model.dto.user.EditPlatformUserDto;
import com.wfr.basic.service.platform.model.dto.user.ListPlatformUserDto;
import com.wfr.basic.service.platform.model.entity.PlatformUserEntity;
import com.wfr.basic.service.platform.model.vo.user.PlatformUserVo;
import com.wfr.basic.service.platform.service.IPlatformUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 平台用户 服务实现层
 *
 * @author wangfarui
 * @since 2022/7/25
 */
@Service
public class PlatformUserServiceImpl implements IPlatformUserService {

    @Autowired
    private IPlatformUserDaoService platformUserDaoService;

    @Override
    public PlatformUserVo getPlatformUserInfoById(Long id) {
        PlatformUserEntity entity = platformUserDaoService.getById(id);
        PlatformUserVo platformUserVo = new PlatformUserVo();
        BeanUtils.copyProperties(entity, platformUserVo);
        return platformUserVo;
    }

    @Override
    @Transactional
    public void registerPlatformUser(EditPlatformUserDto dto) {
        Long count = platformUserDaoService.lambdaQuery()
                .eq(PlatformUserEntity::getAccountName, dto.getAccountName())
                .count();
        if (count > 0) {
            throw new IllegalArgumentException("账户名不能重复");
        }
        PlatformUserEntity entity = new PlatformUserEntity();
        BeanUtils.copyProperties(dto, entity);
        platformUserDaoService.save(entity);
    }

    @Override
    @Transactional
    public void editPlatformUser(EditPlatformUserDto dto) {
        platformUserDaoService.lambdaUpdate()
                .eq(PlatformUserEntity::getId, dto.getId())
                .set(StringUtils.hasText(dto.getName()), PlatformUserEntity::getName, dto.getName())
                .set(StringUtils.hasText(dto.getAccountPassword()), PlatformUserEntity::getAccountPassword, dto.getAccountPassword())
                .set(StringUtils.hasText(dto.getComment()), PlatformUserEntity::getComment, dto.getComment())
                .update();
    }

    @Override
    @Transactional
    public void deletePlatformUser(DeletePlatformUserDto dto) {
        platformUserDaoService.removeBatchByIds(dto.getIds());
    }

    @Override
    public List<PlatformUserVo> listPlatformUser(ListPlatformUserDto dto) {
        // TODO 模拟一个公司id
        Long companyId = 1L;

        return platformUserDaoService.listPlatformUser(dto, companyId);
    }
}
