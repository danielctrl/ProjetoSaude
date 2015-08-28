package projetoSaude.mobile.NOMESISTEMA.Configuration;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 */

public abstract class ConfigurationHelper{

    Context context = null;
    protected SharedPreferences settings = null;

    protected ConfigurationHelper(Context ctx){
        context = ctx;
    }


    //Propriedades STRING
    protected final String getString(String key){
        String val = settings.getString(key, null);
        //testaConfigString(val);
        return val;
    }

    protected final void setString(String key, String val){
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, val);
        editor.commit();
    }

    protected final void testaConfigString(String val){
        if (val == null || TextUtils.isEmpty(val))
            throw new RuntimeException("Defina um valor para a configuracao antes de tentar utiliza-la");
    }
}