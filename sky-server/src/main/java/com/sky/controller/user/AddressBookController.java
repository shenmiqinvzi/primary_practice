package com.sky.controller.user;

import com.sky.dto.AddressBookDTO;
import com.sky.entity.AddressBook;
import com.sky.result.Result;
import com.sky.service.AddressBookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/addressBook")
@Api(tags = "用户端地址簿接口")
@Slf4j
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    @PostMapping("/save")
    @ApiOperation("新增地址")
    public Result save(@RequestBody AddressBookDTO dto) {
        log.info("新增地址：{}", dto);
        addressBookService.save(dto);
        return Result.success();
    }

    @GetMapping("/list")
    @ApiOperation("查询当前用户所有地址")
    public Result<List<AddressBook>> list() {
        List<AddressBook> list = addressBookService.list();
        return Result.success(list);
    }

    @GetMapping("/{id}")
    @ApiOperation("根据ID查询地址")
    public Result<AddressBook> getById(@PathVariable Long id) {
        log.info("查询地址：id={}", id);
        AddressBook addressBook = addressBookService.getById(id);
        return Result.success(addressBook);
    }

    @PutMapping
    @ApiOperation("修改地址")
    public Result update(@RequestBody AddressBookDTO dto) {
        log.info("修改地址：{}", dto);
        addressBookService.update(dto);
        return Result.success();
    }

    @DeleteMapping
    @ApiOperation("删除地址")
    public Result delete(@RequestParam Long id) {
        log.info("删除地址：id={}", id);
        addressBookService.deleteById(id);
        return Result.success();
    }

    @PutMapping("/default")
    @ApiOperation("设置默认地址")
    public Result setDefault(@RequestBody AddressBookDTO dto) {
        log.info("设置默认地址：id={}", dto.getId());
        addressBookService.setDefault(dto.getId());
        return Result.success();
    }

    @GetMapping("/default")
    @ApiOperation("查询默认地址")
    public Result<AddressBook> getDefault() {
        AddressBook addressBook = addressBookService.getDefault();
        return Result.success(addressBook);
    }
}