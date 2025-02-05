package com.rocnarf.rocnarf;

import android.content.Intent;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.SearchView;

import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.adapters.FacturaDetalleRecyclerViewAdapter;
import com.rocnarf.rocnarf.models.Cobro;
import com.rocnarf.rocnarf.models.FacturaDetalle;
import com.rocnarf.rocnarf.models.NotaCredito;

public class FacturaDetalleActivity extends AppCompatActivity
        implements FacturaDetalleFragment.OnListFragmentInteractionListener, CobroFragment.OnListFragmentInteractionListener, NotaCreditoFragment.OnListFragmentInteractionListener {
    private String idCliente, idUsuario, idFactura;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private SearchView searchView;
    private FacturaDetalleRecyclerViewAdapter facturaDetalleRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factura_detalle);

        Intent i = getIntent();
        idUsuario = i.getStringExtra(Common.ARG_IDUSUARIO);
        idCliente = i.getStringExtra(Common.ARG_IDCLIENTE);
        idFactura = i.getStringExtra(Common.ARG_IDFACTURA);


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }

    @Override
    public void onListFragmentInteraction(FacturaDetalle item) {

    }

    @Override
    public void onListFragmentInteraction(Cobro item) {

    }

    public void onListFragmentInteraction(NotaCredito item) {

    }
//    @Override
//    public void onListFragmentInteraction(Cobro item) {
//
//    }
    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if (position == 0) {
                return FacturaDetalleFragment.newInstance(idUsuario, idCliente, idFactura);
            } else if (position == 1) {
                return CobroFragment.newInstance(idUsuario, idCliente, idFactura);
            } else {
                return NotaCreditoFragment.newInstance(idUsuario, idCliente, idFactura);
            }

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return "Productos";
            } else if (position == 1) {
                return "Cobros";
            } else
                return "N/C";
        }
    }

}
