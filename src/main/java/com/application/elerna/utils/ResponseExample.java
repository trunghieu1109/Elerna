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
