package projetoSaude.mobile.NOMESISTEMA.Configuration;

import android.content.Context;

/**
 * Created by Daniel on 28/08/2015.
 */
public class ConnectivitySettings extends ConfigurationHelper{

    //Nome da sessoa de configuracao
    private static final String CONNECTIVITY_SETTINGS = "connectivitySettings";


    //Nome das configuracoes desta sessao
    private static final String BT_MAC_ADDRESS = "btMacAddress";


    public ConnectivitySettings(Context ctx) {
        super(ctx);
        //Pego as configuracoes com o devido nome. Mode 0 significa que apenas esse app tera acesso a esta config
        settings = this.context.getSharedPreferences(CONNECTIVITY_SETTINGS, 0);
    }


    public String getBtMacAddress() {
        return getString(BT_MAC_ADDRESS);
    }

    public void setGetBtMAC(String macAddress) {
        setString(BT_MAC_ADDRESS, macAddress);
    }
}