package puc.sustentar.common;

import com.google.gson.annotations.Expose;

public class Ok {

    @Expose
    private String result = "ok";

    public Ok(String result) {
        this.result = result;
    }

    public Ok() {
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

}

