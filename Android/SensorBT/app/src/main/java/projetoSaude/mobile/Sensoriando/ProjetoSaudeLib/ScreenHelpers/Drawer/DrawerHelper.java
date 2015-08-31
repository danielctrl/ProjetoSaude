package projetoSaude.mobile.Sensoriando.ProjetoSaudeLib.ScreenHelpers.Drawer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import projetoSaude.mobile.Sensoriando.R;

/**
 * Created by Daniel on 31/08/2015.
 */
public class DrawerHelper {

    ListView mDrawerList;
    ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();
    ArrayList<Intent> mNavItemsIntents = new ArrayList<Intent>();
    RelativeLayout mDrawerPane;
    DrawerLayout mDrawerLayout;
    Context mCtx;

    public DrawerHelper(Context ctx, DrawerLayout drawerLayout, RelativeLayout drawerPane, ListView drawerList) {

        mDrawerLayout = drawerLayout;
        mDrawerPane = drawerPane;
        mDrawerList = drawerList;
        mCtx = ctx;

        mDrawerList.setAdapter(new DrawerListAdapter(this.mCtx, mNavItems));

        // Drawer Item click listeners
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItemFromDrawer(position);
            }
        });
    }

    //Posteriormente poderemos trocar este Intent actionOnClickIntent para alguma classe propria que fara coisas pertinentes
    public void createItem(String title, String subTitile, Intent actionOnClickIntent){
        mNavItems.add(new NavItem(title, subTitile));
        mNavItemsIntents.add(actionOnClickIntent);
    }

    private void selectItemFromDrawer(int position) {
        if (mNavItemsIntents.get(position) != null)
            mCtx.startActivity(mNavItemsIntents.get(position));
    }
}
