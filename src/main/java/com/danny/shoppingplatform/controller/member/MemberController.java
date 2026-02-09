package com.danny.shoppingplatform.controller.member;

import com.danny.shoppingplatform.service.MemberService;
import com.danny.shoppingplatform.service.external.MemberExternalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberController {
    @Autowired
    private MemberExternalService memberExternalService;


}
