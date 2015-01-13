using System;
using Android.App;
using Android.Content;
using Android.Runtime;
using Android.Views;
using Android.Widget;
using Android.OS;
using Android.Bluetooth;
using System.Linq;
using Java.Util;
using System.Threading.Tasks;
using System.Collections.Generic;
using System.Globalization;

namespace BT
{
    [Activity(Label = "BT", MainLauncher = true, Icon = "@drawable/icon")]
    public class MainActivity : Activity
    {
        BluetoothSocket _socket = null;
        BluetoothDevice device = null;
        BluetoothAdapter adapter = null;
        Handler _handler = new Handler();

        //Constantes
        public const int STATE_NONE = 0;
        public const int STATE_LISTEN = 1;
        public const int STATE_CONNECTING = 2;
        public const int STATE_CONNECTED = 3;

        //Componentes
        TextView txtTemp0 = null;
        TextView txtTemp1 = null;
        Button btnConnect = null;

        protected override void OnCreate(Bundle bundle)
        {
            base.OnCreate(bundle);

            // Seta a view
            SetContentView(Resource.Layout.Main);

            // Seta os componentes da tela
            txtTemp0 = FindViewById<TextView>(Resource.Id.txtTemp0);
            txtTemp1 = FindViewById<TextView>(Resource.Id.txtTemp1);
            btnConnect = FindViewById<Button>(Resource.Id.btnConnect);

            //Seta os eventos do componentes
            btnConnect.Click += btnConnect_Click;


            //Starta o BT
            adapter = BluetoothAdapter.DefaultAdapter;
            if (adapter == null)
                throw new Exception("No Bluetooth adapter found.");

            if (!adapter.IsEnabled)
                throw new Exception("Bluetooth adapter is not enabled.");

            device = (from bd in adapter.BondedDevices
                      where bd.Name == "HC-06"
                      select bd).FirstOrDefault();

            if (device == null)
                throw new Exception("Named device not found.");

            _socket = device.CreateRfcommSocketToServiceRecord(UUID.FromString("00001101-0000-1000-8000-00805f9b34fb"));

            //PegaDadosBT(_socket);
        }


        public void btnConnect_Click(object sender, EventArgs e)
        {
            Toast.MakeText(this, "Iniciando procedimento.", ToastLength.Short).Show();

            PegaDadosBT(_socket);
        }

        public void PegaDadosBT(BluetoothSocket _socket)
        {
            try
            {
                _socket.Connect();
                //Toast.MakeText(this, "Conectado.", ToastLength.Short).Show();
            }
            catch (Exception ex)
            {
                //Toast.MakeText(this, "Algum erro para conectar com o BT.", ToastLength.Long);
                throw new Exception("Algum erro para conectar com o BT.");
            }

            Task.Factory.StartNew(() =>
                {

                    int bytes;
                    byte[] buffer = new byte[1024];
                    List<string> temps = new List<string>() { "-", "-" };
                    List<bool> isTempsCorretas = new List<bool>() { false, false };

                    while (true)
                    {
                        System.Threading.Thread.Sleep(500);
                        try
                        {
                            bytes = _socket.InputStream.Read(buffer, 0, buffer.Length);

                            String readMessage = System.Text.Encoding.ASCII.GetString(buffer).Trim();
                            
                            OrganizaTextoDoBuffer(readMessage, temps, isTempsCorretas);

                            if (IsPodeAtualizar(isTempsCorretas))
                            {
                                for (int i = 0; i < temps.Count; i++)
                                {
                                    RunOnUiThread(() =>
                                        {
                                            txtTemp0.Text = temps[0];
                                            txtTemp1.Text = temps[1];
                                            //throw new Exception("O sensor numero " + i + " ainda não foi formatado na tela.");
                                        });
                                }
                                ResetaIsTempsCorretas(isTempsCorretas);
                            }
                        }
                        catch (Exception ex)
                        {
                            RunOnUiThread(() =>
                                    {
                                        //Toast.MakeText(this, "Algum erro para pegar dados.", ToastLength.Long);
                                        throw new Exception("Algum erro para pegar dados.");
                                    });
                        }
                    }
                });
        }

        private void OrganizaTextoDoBuffer(string readMessage, List<string> listaTemp, List<bool> listaIsTempsCorretas)
        {
            int posicaoInicial = 0;
            for (int i = 0; i < listaTemp.Count; i++)
            {
                posicaoInicial = readMessage.IndexOf(i + " --") + 5;

                //Verifico se achou o "id" do sensor
                //Verifico se na mensagem esta a temperatura completa
                if (posicaoInicial != -1)
                    if (readMessage.Substring(posicaoInicial, 15).IndexOf("\n\r") != -1)
                    {
                        try
                        {
                            listaTemp[i] = Convert.ToDouble(readMessage.Substring(posicaoInicial, 5).Replace(",", "."), CultureInfo.InvariantCulture).ToString();
                            listaIsTempsCorretas[i] = true;
                        }
                        catch (Exception)
                        {
                            Toast.MakeText(this, "Erro ao converter o dado para numero", ToastLength.Long);
                            throw new Exception("Erro ao converter o dado para numero");
                        }
                    }
            }
        }

        private bool IsPodeAtualizar(List<bool> listaIsTempsCorretas)
        {
            foreach (var isCorreta in listaIsTempsCorretas)
                if (isCorreta == false)
                    return false;

            return true;
        }

        private void ResetaIsTempsCorretas(List<bool> listaIsTempsCorretas)
        {
            for (int i = 0; i < listaIsTempsCorretas.Count; i++)
                listaIsTempsCorretas[i] = false;
        }

    }
}

