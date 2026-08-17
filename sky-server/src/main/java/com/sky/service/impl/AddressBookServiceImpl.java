package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.AddressBookDTO;
import com.sky.entity.AddressBook;
import com.sky.exception.BaseException;
import com.sky.mapper.AddressBookMapper;
import com.sky.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class AddressBookServiceImpl implements AddressBookService {

    @Autowired
    private AddressBookMapper addressBookMapper;

    @Override
    public void save(AddressBookDTO dto) {
    Long userId = BaseContext.getCurrentId();

    // 判断该用户是否已有地址
    List<AddressBook> existing = addressBookMapper.listByUserId(userId);
    boolean isFirst = (existing == null || existing.isEmpty());

    AddressBook addressBook = new AddressBook();
    BeanUtils.copyProperties(dto, addressBook);
    addressBook.setUserId(userId);

    // 如果是首个地址，自动设为默认
    if (isFirst) {
        addressBook.setIsDefault(1);
    } else {
        // 如果前端传了 isDefault 且为 1，需要先清空再设置，但一般新增时不传，这里设 0
        addressBook.setIsDefault(0);
    }

    addressBookMapper.insert(addressBook);
    log.info("新增地址成功：{}", addressBook);
    }

    @Override
    public List<AddressBook> list() {
        Long userId = BaseContext.getCurrentId();
        return addressBookMapper.listByUserId(userId);
    }

    @Override
    public AddressBook getById(Long id) {
        AddressBook addressBook = addressBookMapper.getById(id);
        // 简单校验：地址是否存在，后续可以加上权限校验（该地址是否属于当前用户）
        if (addressBook == null) {
            throw new BaseException("地址不存在");
        }
        return addressBook;
    }

    @Override
    public void update(AddressBookDTO dto) {
        AddressBook addressBook = new AddressBook();
        BeanUtils.copyProperties(dto, addressBook);
        addressBookMapper.update(addressBook);
        log.info("修改地址成功：{}", addressBook);
    }

    @Override
    public void deleteById(Long id) {
        // 校验地址是否存在
        AddressBook addressBook = addressBookMapper.getById(id);
        if (addressBook == null) {
            throw new BaseException("地址不存在，无法删除");
        }
        addressBookMapper.deleteById(id);
        log.info("删除地址成功：id={}", id);
    }

    @Override
    @Transactional
    public void setDefault(Long id) {
        Long userId = BaseContext.getCurrentId();

        // 1. 校验要设置的地址是否存在
        AddressBook addressBook = addressBookMapper.getById(id);
        if (addressBook == null) {
            throw new BaseException("地址不存在，无法设为默认");
        }

        // 2. 将该用户所有地址的 is_default 置为 0
        addressBookMapper.clearDefaultByUserId(userId);

        // 3. 将指定地址设为默认（is_default = 1）
        addressBookMapper.setDefaultById(id);

        log.info("设置默认地址成功：userId={}, addressId={}", userId, id);
    }

    @Override
    public AddressBook getDefault() {
        Long userId = BaseContext.getCurrentId();
        AddressBook addressBook = addressBookMapper.getDefaultByUserId(userId);
        // 注意：可能返回 null（用户从未设置默认地址）
        return addressBook;
    }
}