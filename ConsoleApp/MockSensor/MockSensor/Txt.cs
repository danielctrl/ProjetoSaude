using MockSensor.Model;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Globalization;
using System.Threading.Tasks;

namespace MockSensor
{
    public static class Txt
    {

        public static void AcionaTextoNoArquivo(string path, List<Conjunto> conjuntos)
        {

            foreach (var conjunto in conjuntos)
            {
                foreach (var sensor in conjunto.sensores)
                {
                    string idSensor = "c" + conjunto.id + "-" + "s" + sensor.id;
                    string pathIdSensor = string.Format("{0}\\{1}\\", path, idSensor);
                    string nomeArquivo = DateTime.Now.ToString("hh") + ".txt";

                    try
                    {
                        //testo se não existe o diretorio para cria-lo
                        if (!Directory.Exists(pathIdSensor))
                            Directory.CreateDirectory(pathIdSensor);

                        //este metodo adiciona o arquivo no diretorio caso ele não exista
                        StreamWriter arquivo = new StreamWriter(pathIdSensor + nomeArquivo, true);

                        var texto = DateTime.Now.ToString("hh:mm:ss.fffff", CultureInfo.InvariantCulture) + "\t"
                                    + "t: " + sensor.temp.ToString("F5").Replace(".", ",") + Environment.NewLine;

                        arquivo.Write(texto);

                        arquivo.Flush();
                        arquivo.Close();
                    }
                    catch (Exception)
                    {
                        throw new Exception("Não foi possível editar/criar o arquivo: " + path);
                    }
                }
            }
        }
    }
}
