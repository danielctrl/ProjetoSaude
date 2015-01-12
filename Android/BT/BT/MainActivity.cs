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

namespace BT
{
    [Activity(Label = "BT", MainLauncher = true, Icon = "@drawable/icon")]
    public class MainActivity : Activity
    {
        BluetoothSocket _socket = null;
        BluetoothDevice device = null;
        BluetoothAdapter adapter = null;

        //Componentes
        TextView txtTemp = null;
        Button btnConnect = null;

        //Variaveis de controle do texto de tela
        byte[] buffer = null;
        string temp1 = null;
        string temp2 = null;

        protected override void OnCreate(Bundle bundle)
        {
            base.OnCreate(bundle);

            // Seta a view
            SetContentView(Resource.Layout.Main);

            // Seta os componentes da tela
            txtTemp = FindViewById<TextView>(Resource.Id.txtTemp);
            btnConnect = FindViewById<Button>(Resource.Id.btnConnect);

            //Seta os eventos do componentes
            //btnConnect.Click += btnConnect_Click;


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

            PegaDadosBT(_socket);
        }


        public void btnConnect_Click(object sender, EventArgs e)
        {
            Toast.MakeText(this, "Capturando dados", ToastLength.Long).Show();

            Task.Factory.StartNew(() =>
                    {
                        Console.WriteLine("Hello from taskA.");
                        PegaDadosBT(_socket);
                    });
        }

        public async void PegaDadosBT(BluetoothSocket _socket)
        {
            try
            {
                _socket.Connect();
            }
            catch (Exception ex)
            {
                throw;
            }
            buffer = new byte[1024];

            while (true)
            {
                try
                {
                    await _socket.InputStream.ReadAsync(buffer, 0, buffer.Length);
                    string bufferAtual = System.Text.Encoding.UTF8.GetString(buffer);
                    buffer = new byte[1024];
                }
                catch (Exception ex)
                {
                    break;
                }
            }
        }

    }

    //public class asddd : 
}

