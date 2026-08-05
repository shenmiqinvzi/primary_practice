package com.sky.result;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.io.Serializable;


@Data
@AllArgsConstructor
public class PageResult implements Serializable {
    private Long total;
    private List records;
}
