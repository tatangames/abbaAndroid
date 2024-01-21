package com.tatanstudios.abba.adaptadores.planes.cuestionario;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.tatanstudios.abba.fragmentos.planes.cuestionario.FragmentCuestionarioPlanBloque;
import com.tatanstudios.abba.fragmentos.planes.cuestionario.FragmentPreguntasPlanBloque;

public class MiPagerCuestionarioAdapter extends FragmentStateAdapter {

    private int numberOfItems, idBloqueDeta;

    public MiPagerCuestionarioAdapter(FragmentActivity fragmentActivity, int numberOfItems, int idBloqueDeta) {
        super(fragmentActivity);

        this.numberOfItems = numberOfItems;
        this.idBloqueDeta = idBloqueDeta;
    }

    @Override
    public Fragment createFragment(int position) {
        if(numberOfItems == 1){
            switch (position) {
                case 0:
                    return FragmentCuestionarioPlanBloque.newInstance(idBloqueDeta);
                default:
                    return null;
            }
        }else{
            switch (position) {
                case 0:
                    return FragmentCuestionarioPlanBloque.newInstance(idBloqueDeta);
                case 1:
                    return FragmentPreguntasPlanBloque.newInstance(idBloqueDeta);
                default:
                    return null;
            }
        }
    }

    @Override
    public int getItemCount() {
        return numberOfItems; // Número total de fragmentos
    }
}