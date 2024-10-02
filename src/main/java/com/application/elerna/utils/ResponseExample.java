package com.application.elerna.utils;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class ResponseExample {

    public static final String getAllUserBySearchExample = """
                                            {
                                              "status": 200,
                                              "pageNo": 0,
                                              "pageSize": 2,
                                              "totalPages": 1,
                                              "data": [
                                                {
                                                  "firstName": "Hieu",
                                                  "lastName": "Nguyen",
                                                  "dateOfBirth": "2004-08-10",
                                                  "username": "Username is hidden",
                                                  "password": "Password is hidden",
                                                  "address": "Viet Tri",
                                                  "phone": "23452345",
                                                  "email": "user02101@gmail.com",
                                                  "cardNumber": "5234523452345234",
                                                  "cardHolder": "Hieu Nguyen",
                                                  "amount": 100
                                                }
                                              ]
                                            }
                                            """;

    public static final String getAllUserBySortExample = """
                                            {
                                              "status": 200,
                                              "pageNo": 0,
                                              "pageSize": 2,
                                              "totalPages": 1,
                                              "data": [
                                                {
                                                  "firstName": "Hieu",
                                                  "lastName": "Nguyen",
                                                  "dateOfBirth": "2004-08-10",
                                                  "username": "Username is hidden",
                                                  "password": "Password is hidden",
                                                  "address": "Viet Tri",
                                                  "phone": "23452345",
                                                  "email": "user02101@gmail.com",
                                                  "cardNumber": "5234523452345234",
                                                  "cardHolder": "Hieu Nguyen",
                                                  "amount": 100
                                                }
                                              ]
                                            }
                                            """;

    public static final String getUserDetailExample = """
            {
              "status": 200,
              "message": "Get user's details with userId 44",
              "data": {
                "firstName": "Dat",
                "lastName": "Nguyen",
                "dateOfBirth": "2004-08-10",
                "username": "Username is hidden",
                "password": "Password is hidden",
                "address": "Phu Tho",
                "phone": "546545",
                "email": "user02102@gmail.com",
                "cardNumber": "23453453456",
                "cardHolder": "Dat Nguyen",
                "amount": 100
              }
            }
            """;

    public static final String updateUserExample = """
            {
              "status": 200,
              "message": "Update user",
              "data": {
                "firstName": "Trung",
                "lastName": "Nguyen",
                "dateOfBirth": "2005-08-10T00:00:00.000+00:00",
                "username": "Username is hidden",
                "password": "Password is hidden",
                "address": "Trung Vuong",
                "phone": "54654654",
                "email": "kunno@gmail.com",
                "cardNumber": "56465456",
                "cardHolder": "Dat Nguyen",
                "amount": 100
              }
            }
            """;

    public static final String deleteUserExample = """
            {
              "status": 202,
              "message": "Delete user 23 successfully",
              "data": null
            }
            """;

    public static final String depositExample = """
            {
              "status": 202,
              "message": "Deposit money into bank account, amount = 999.0",
              "data": "Deposit money to bank account 17, amount = 999.0, residual = 1099.0"
            }
            """;

    public static final String payExample = """
            {
              "status": 200,
              "message": "Pay for course",
              "data": "User 34 pays for course 22 successfully"
            }
            """;

    public static final String getAllTransactionExample = """
            {
              "status": 200,
              "pageNo": 0,
              "pageSize": 2,
              "totalPages": 3,
              "data": [
                {
                  "userId": 35,
                  "cardHolder": "admin account",
                  "email": "admin@gmail.com",
                  "phone": "032326561",
                  "courseId": 17,
                  "description": "This payment is implemented by credit",
                  "price": 53,
                  "paymentMethod": "By credit",
                  "cardNumber": "9120352874",
                  "createAt": "2024-09-30T19:05:57.000+00:00",
                  "updateAt": "2024-09-30T19:05:57.000+00:00"
                },
                {
                  "userId": 39,
                  "cardHolder": "admin account",
                  "email": "admin@gmail.com",
                  "phone": "032326561",
                  "courseId": 17,
                  "description": "This payment is implemented by credit",
                  "price": 56,
                  "paymentMethod": "By credit",
                  "cardNumber": "9120352874",
                  "createAt": "2024-10-01T05:53:28.000+00:00",
                  "updateAt": "2024-10-01T05:53:28.000+00:00"
                }
              ]
            }
            """;

    public static final String getTransactionDetailsExample = """
            {
              "status": 200,
              "message": "Get transaction details successfully",
              "data": {
                "userId": 41,
                "cardHolder": "admin account",
                "email": "admin@gmail.com",
                "phone": "032326561",
                "courseId": 20,
                "description": "Pay for registering course 20",
                "price": 100,
                "paymentMethod": "By credit",
                "cardNumber": "9120352874",
                "createAt": "2024-10-01T09:22:22.000+00:00",
                "updateAt": "2024-10-01T09:22:22.000+00:00"
              }
            }
            """;

    public static final String getTransactionHistoryExample = """
            {
              "status": 200,
              "pageNo": 0,
              "pageSize": 2,
              "totalPages": 1,
              "data": [
                {
                  "userId": 34,
                  "cardHolder": "admin account",
                  "email": "admin@gmail.com",
                  "phone": "032326561",
                  "courseId": 22,
                  "description": "Pay for course 22",
                  "price": 0,
                  "paymentMethod": "By credit",
                  "cardNumber": "9120352874",
                  "createAt": "2024-10-02T04:06:54.000+00:00",
                  "updateAt": "2024-10-02T04:06:54.000+00:00"
                }
              ]
            }
            """;

    public static final String getBankAccountLogs = """
            {
              "status": 200,
              "pageNo": 0,
              "pageSize": 5,
              "totalPages": 1,
              "data": [
                {
                  "messageId": 6,
                  "messageType": "Deposit",
                  "message": "Deposit money to bank account 17, amount = 999.0, residual = 1099.0",
                  "createdAt": "2024-10-02T04:05:38.000+00:00",
                  "updatedAt": "2024-10-02T04:05:38.000+00:00"
                },
                {
                  "messageId": 7,
                  "messageType": "Payment",
                  "message": "Account 17 pays for course 22 successfully, amount = 0.0, residual = 1099.0",
                  "createdAt": "2024-10-02T04:06:54.000+00:00",
                  "updatedAt": "2024-10-02T04:06:54.000+00:00"
                }
              ]
            }
            """;

    public static final String loginExample = """
                                            {
                                              "status": 202,
                                              "message": "User login. Get access token successully",
                                              "data": {
                                                "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzeXN0ZW1hZG1pbiIsImlhdCI6MTcyNzg0MDczNCwiZXhwIjoxNzI3ODQ0MzM0fQ.jqDREefe8rX21Elx0tbUXwVXHZgVpPxJzmfwvUL-pqo",
                                                "refreshToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzeXN0ZW1hZG1pbiIsImlhdCI6MTcyNzg0MDczNCwiZXhwIjoxNzI3ODQ0MzM0fQ.lkNtH7IQqqLuonJXTPVdqPLOaft22PD5EqnJfDXKr00",
                                                "resetToken": "reset_token",
                                                "userId": 34
                                              }
                                            }
                                            """;

}
