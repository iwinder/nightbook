package com.windcoder.nightbook.mina.controller;

import com.windcoder.nightbook.mina.entity.WXHeader;
import com.windcoder.nightbook.mina.exception.MinaAuthorizationAPIException;
import com.windcoder.nightbook.mina.service.MinaLoginService;
import com.windcoder.nightbook.mina.utils.Constants;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/auth/")
public class MinaLoginController {

    @Autowired
    MinaLoginService minaLoginService;

    @RequestMapping("home")
    public JSONObject home(HttpServletRequest request) {
        WXHeader header = new WXHeader();
        header.setPid(Long.valueOf(request.getHeader(Constants.WX_HEADER_PID)));
        header.setJcode(request.getHeader(Constants.WX_HEADER_CODE));
        header.setEncryptData(request.getHeader(Constants.WX_HEADER_ENCRYPTED_DATA));
        header.setIv(request.getHeader(Constants.WX_HEADER_IV));
        JSONObject j = new JSONObject();
        try {
           j =  minaLoginService.doLoginMain( header.getPid(), header.getJcode(), header.getEncryptData(), header.getIv());
        } catch (MinaAuthorizationAPIException e) {
            e.printStackTrace();
        }
        return j;
    }
}
