package com.windcoder.nightbook.common.utils.douBan;

import lombok.Data;

@Data
public class DouBanBookSearch {
    private String keys;
    private Long start;
    private Long count;
    private Long total;
    private Long uuid;
}
