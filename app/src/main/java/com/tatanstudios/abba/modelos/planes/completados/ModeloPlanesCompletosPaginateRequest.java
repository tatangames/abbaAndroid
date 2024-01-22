package com.tatanstudios.abba.modelos.planes.completados;

import com.google.gson.annotations.SerializedName;

public class ModeloPlanesCompletosPaginateRequest {

    @SerializedName("page")
    private int page;

    @SerializedName("limit")
    private int limit;

    @SerializedName("iduser")
    private int iduser;

    @SerializedName("idiomaplan")
    private int idiomaplan;

    public int getPage() {
        return page;
    }

    public int getLimit() {
        return limit;
    }

    public int getIduser() {
        return iduser;
    }

    public int getIdiomaplan() {
        return idiomaplan;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setIduser(int iduser) {
        this.iduser = iduser;
    }

    public void setIdiomaplan(int idiomaplan) {
        this.idiomaplan = idiomaplan;
    }

}
