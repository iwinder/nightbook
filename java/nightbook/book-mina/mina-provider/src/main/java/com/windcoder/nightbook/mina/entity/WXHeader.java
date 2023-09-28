package com.windcoder.nightbook.mina.entity;

import lombok.Data;

@Data
public class WXHeader {
    private Long id;
    private Long pid;
    private String jcode;
    private String encryptData;
    private String iv;
}
