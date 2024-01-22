package com.tatanstudios.abba.modelos.planes.completados;

import com.google.gson.annotations.SerializedName;

public class ModeloPlanesCompletosPaginateApi<T>{

    @SerializedName("success")
    private int success;

    @SerializedName("hayinfo")
    private int hayinfo;

    @SerializedName("listaplanes")
    private T data;

    // Otros campos según sea necesario, por ejemplo, metadatos de paginación



    public T getData() {
        return data;
    }

    public int getSuccess() {
        return success;
    }

    public int getHayinfo() {
        return hayinfo;
    }
}
