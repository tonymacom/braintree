package com.tony.unit.braintree;

import com.alibaba.fastjson.JSON;
import com.braintreegateway.*;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class BraintreeApplicationTests {


    public static BraintreeGateway gateway = new BraintreeGateway(
            Environment.SANDBOX,
            "vjhdxcmzf2bjq7y9", //merchant_id
            "y2sw4b8qpnxpxm7v", //Public Key
            "b804d032990e2b99e6f8093d61aed766" //private key
    );

    public static ClientTokenRequest clientTokenRequest = new ClientTokenRequest()
            .customerId("551309871");

    @Test
    public void createClientToken() {
        ClientTokenRequest clientTokenRequest = new ClientTokenRequest()
                .customerId("283304601");

        String clientToken = gateway.clientToken().generate(clientTokenRequest);
        System.out.println(clientToken);
    }






    /**
     * 创建custom
     */
    @Test
    public void createCustom(){

        CustomerRequest request = new CustomerRequest()
                .id("585759")
                .firstName("tony")
                .lastName("ma")
                .company("yami Co.")
                .email("ittony@example.com")
                .fax("419-555-1234")
                .phone("614-555-1234")
                .website("http://example.com")
                ;
        Result<Customer> result = gateway.customer().create(request);

        if(result.isSuccess()){
            System.out.println(result.getTarget().getId());
        }
    }

    /**
     * 绑定客户信用卡
     */
    @Test
    public void createCreditCard(){

        CreditCardRequest request = new CreditCardRequest()
                .customerId("283304601")
                .cvv("100")
                .number("4111111111111111")
                .token("abcd")
                .expirationDate("06/22")
                .billingAddress()
                .firstName("tony2")
                .done()
                ;
        Result<CreditCard> result = gateway.creditCard().create(request);
        System.out.println(JSON.toJSONString(result));
    }

    @Test
    public void updateCreditCard(){

        CreditCardRequest request = new CreditCardRequest()
                .cardholderName("tony2")
                .customerId("283304601")
                .cvv("")
                .number("4111111111111111")
//                .token("abcd")
                .expirationDate("06/22")
//                .billingAddress()
//                .firstName("tony22")
//                .done()
                ;
        Result<CreditCard> updateResult = gateway.creditCard().update("abcd", request);
        System.out.println(JSON.toJSONString(updateResult));
    }


    @Test
    public void testAddress(){
        AddressRequest request = new AddressRequest()
                .firstName("Jenna")
                .lastName("Smith")
                .company("Braintree")
                .streetAddress("1 E Main St")
                .extendedAddress("Suite 403")
                .locality("Chicago")
                .region("Illinois")
                .postalCode("60622")
                .countryCodeAlpha2("US");

        Result<Address> result = gateway.address().create("678755", request);

        System.out.println(JSON.toJSONString(result));

    }


    @Test
    public void findCard(){
        CreditCard creditCard = gateway.creditCard().find("3cjxfn");
        Address address = creditCard.getBillingAddress();
        System.out.println(JSON.toJSONString(address));

    }


    /**
     * 支付
     */
    @Test
    public void charge(){
        TransactionRequest  saleRequest = new TransactionRequest()
                .customerId("283304601")
                .amount(new BigDecimal(10.00))
                .orderId("aaaaaorderId")
                .paymentMethodToken("3p3x27")
                .creditCard()
                .cvv("2000")
                .done()
                .options()
                .submitForSettlement(true)
                .done();
        Result<Transaction> result = gateway.transaction().sale(saleRequest);
        System.out.println(JSON.toJSONString(result));
    }



    /**
     * 折扣卡
     */
    @Test
    public void discount(){
        List<Discount> discounts = gateway.discount().all();
        System.out.println(JSON.toJSONString(discounts));
    }

    /**
     * 获取支付nonce
     *
     */
    @Test
    public void getNonce() {

        Result<PaymentMethodNonce> result = gateway.paymentMethodNonce().create("kcx9dhd");
        System.out.println(result.getTarget().getNonce());
    }


    //error
    //
    @Test
    public void findPaymentMethodNonce(){
        PaymentMethodNonce paymentMethodNonce = gateway.paymentMethodNonce().find("5e7c7b58-2e44-0727-7802-85e7ebf3728c");
        //3D信用卡认证
        ThreeDSecureInfo info = paymentMethodNonce.getThreeDSecureInfo();
        if (info == null) {
            return; // This means that the nonce was not 3D Secured
        }

        System.out.println(info.getEnrolled());
        System.out.println(info.isLiabilityShifted());
        System.out.println(info.isLiabilityShiftPossible());
        System.out.println(info.getStatus());
    }

    /**
     * 结算
     */
    @Test
    public void settlement(){
        Result<SettlementBatchSummary> result = gateway
                .settlementBatchSummary()
                .generate(Calendar.getInstance());

        if (result.isSuccess()) {
            List<Map<String,String>> records = result.getTarget().getRecords();
            System.out.println(JSON.toJSONString(records));
        }
    }

    /**
     * 信用卡校验
     */
    @Test
    public void creditCardVerify(){
        CreditCardVerificationSearchRequest request = new CreditCardVerificationSearchRequest()
                .id().is("6wgmyans");

        ResourceCollection<CreditCardVerification> collection = gateway.creditCardVerification().search(request);

        for (CreditCardVerification verification : collection) {
            System.out.println(JSON.toJSONString(verification));
        }
    }


}
