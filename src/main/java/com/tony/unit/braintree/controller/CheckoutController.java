package com.tony.unit.braintree.controller;

import com.braintreegateway.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;


/**
 * <br>
 * <b>功能：</b><br>
 * <b>作者：</b>www.yamibuy.com<br>
 * <b>日期：</b> 2018/11/6 <br>
 * <b>版权所有：</b>版权所有(C) 2016，www.yamibuy.com<br>
 */
@Slf4j
@RestController
public class CheckoutController {

    public static BraintreeGateway gateway = new BraintreeGateway(
            Environment.SANDBOX,
            "vjhdxcmzf2bjq7y9", //merchant_id
            "y2sw4b8qpnxpxm7v", //Public Key
            "b804d032990e2b99e6f8093d61aed766" //private key
    );

    @GetMapping("/getTokens")
    public Object getTokens(HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Origin", "*");

        ClientTokenRequest clientTokenRequest = new ClientTokenRequest()
                .customerId("283304601");

        String clientToken = gateway.clientToken().generate();

        Map<String,String> tokens = new HashMap<>();
        tokens.put("rightToken",clientToken);

        return tokens;

    }

    /**
     * 结账
     * @param paymentMethodNonce
     * @param response
     * @return
     */
    @PostMapping("/checkout")
    public Object checkout(@RequestParam("paymentMethodNonce") String paymentMethodNonce, HttpServletResponse response){

        TransactionRequest request = new TransactionRequest()
                .amount(new BigDecimal("10.00"))
                .orderId("20180110801")
                .paymentMethodNonce(paymentMethodNonce)
                .options()
                .submitForSettlement(true)
                .done();

        Result<Transaction> result = gateway.transaction().sale(request);


        response.setHeader("Access-Control-Allow-Origin", "*");

        return  result ;
    }

}
