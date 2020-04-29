// ******************************************************************
// * Copyright (C) 2018 DigiCash Technology Ventures                *
// *                                                                *
// * All rights reserved. This file is proprietary and              *
// * confidential and can not be copied and/or distributed          *
// * without the express permission of DigiCash Technology Ventures *
// ******************************************************************

package puc.sustentar.auth;

import com.google.gson.annotations.Expose;

public class Login {

    @Expose
    private String email;

    @Expose
    private String password;
    public Login() {
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
