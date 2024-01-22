package com.tatanstudios.abba.modelos.planes.completados;

import com.google.gson.annotations.SerializedName;
import com.tatanstudios.abba.modelos.planes.ModeloPlanes;

import java.util.List;

public class ModeloPlanesCompletadoContenedor {


    @SerializedName("current_page")
    public Integer currentPage;
    @SerializedName("data")
    public List<ModeloPlanes> data = null;
    @SerializedName("first_page_url")
    public String firstPageUrl;
    @SerializedName("from")
    public Integer from;
    @SerializedName("last_page")
    public Integer lastPage;
    @SerializedName("last_page_url")
    public String lastPageUrl;

    @SerializedName("next_page_url")
    public String nextPageUrl;
    @SerializedName("path")
    public String path;
    @SerializedName("per_page")
    public Integer perPage;
    @SerializedName("prev_page_url")
    public String prevPageUrl;
    @SerializedName("to")
    public Integer to;
    @SerializedName("total")
    public Integer total;


    public Integer getCurrentPage() {
        return currentPage;
    }

    public List<ModeloPlanes> getData() {
        return data;
    }

    public String getFirstPageUrl() {
        return firstPageUrl;
    }

    public Integer getFrom() {
        return from;
    }

    public Integer getLastPage() {
        return lastPage;
    }

    public String getLastPageUrl() {
        return lastPageUrl;
    }

    public String getNextPageUrl() {
        return nextPageUrl;
    }

    public String getPath() {
        return path;
    }

    public Integer getPerPage() {
        return perPage;
    }

    public String getPrevPageUrl() {
        return prevPageUrl;
    }

    public Integer getTo() {
        return to;
    }

    public Integer getTotal() {
        return total;
    }
}
