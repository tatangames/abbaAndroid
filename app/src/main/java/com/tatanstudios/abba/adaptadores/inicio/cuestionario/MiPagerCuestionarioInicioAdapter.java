package com.tatanstudios.abba.adaptadores.inicio.cuestionario;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.tatanstudios.abba.fragmentos.inicio.cuestionario.FragmentCuestionarioInicioBloque;
import com.tatanstudios.abba.fragmentos.inicio.cuestionario.FragmentCuestionarioInicioPregunta;
import com.tatanstudios.abba.fragmentos.planes.cuestionario.FragmentCuestionarioPlanBloque;
import com.tatanstudios.abba.fragmentos.planes.cuestionario.FragmentPreguntasPlanBloque;

public class MiPagerCuestionarioInicioAdapter extends FragmentStateAdapter {

    private int idBloqueDeta;

    public MiPagerCuestionarioInicioAdapter(FragmentActivity fragmentActivity, int idBloqueDeta) {
        super(fragmentActivity);
        this.idBloqueDeta = idBloqueDeta;
    }

    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return FragmentCuestionarioInicioBloque.newInstance(idBloqueDeta);
            case 1:
                return FragmentCuestionarioInicioPregunta.newInstance(idBloqueDeta);
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 2; // Número total de fragmentos
    }
}