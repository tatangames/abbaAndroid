package com.tatanstudios.abba.fragmentos.planes.bloques;

import java.util.List;

public class ItemModel {
    public static final int TIPO_IMAGEN = 0;
    public static final int TIPO_RECYCLER = 1;

    int tipo;
    int imagenResId; // Recurso de imagen (para TIPO_IMAGEN)
    List<SubItemModel> subItems; // Datos para el RecyclerView (para TIPO_RECYCLER)

    boolean seleccionado; // Para determinar si el elemento está seleccionado o no

    public ItemModel(int tipo, int imagenResId, List<SubItemModel> subItems) {
        this.tipo = tipo;
        this.imagenResId = imagenResId;
        this.subItems = subItems;
        this.seleccionado = false;
    }

    public int getTipo() {
        return tipo;
    }

    public int getImagenResId() {
        return imagenResId;
    }

    public List<SubItemModel> getSubItems() {
        return subItems;
    }
}
