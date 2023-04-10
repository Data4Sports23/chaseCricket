package com.data4sports.chasecricket.apiClients;


public class ErrorCodeDisplay {
    //Error code identification
    public static String error(int errorCode) {
        switch (errorCode) {
            case 400:
                // BadRequestBadRequest
                return "invalid inputs";
            case 401:
                // Unauthorized
                return "login first";

            case 402:
                // PaymentRequired
                return "PaymentRequired";

            case 403:
                // Forbidden
                return "Please provide a valid X-API-KEY header.";

            case 404:
                // NotFound
                return "Not found";

            case 405:
                // MethodNotAllowed
                return "Requested HTTP Method is not allowed";

            case 406:
                // NotAcceptable
                return "NotAcceptable";

            case 409:
                // Conflict
                return "Email / Phone No already registered";

            case 411:
                // not match user name and password
                return "User name / password do not match ...";
            case 412:
                // Account not verify
                return "Account not verified";

            case 422:
                // unProcessableEntity
                return "Failed";

            case 423:
                // const Locked = 423
                return "privacy base locked";
            default:
                return "Something went wrong,Please try again";

        }
    }
}