using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using MockSensor.Model;
using System.IO;

namespace MockSensor
{
    class Program
    {
        static void Main(string[] args)
        {
            //Populo
            var conjuntos = Mock.PopulaConjuntosDefinivel(new int[] { 2, 3 }, 20);

            while (true)
            {
                Console.Write("Quantas repeticoes: ");
                int repeticoes = Convert.ToInt32(Console.ReadLine());

                Console.Write("Qual variacao de temp: ");
                double variacao = Convert.ToDouble(Console.ReadLine().Replace(".", ","));

                for (int i = 0; i <= repeticoes; i++)
                {
                    Conjunto.TrocaTempGeral(conjuntos, variacao);

                    //Criando o caminho do arquivo e o arquivo
                    string path = Environment.GetFolderPath(Environment.SpecialFolder.Desktop)
                        + "\\MockSensor\\" + DateTime.Now.ToString("yyyy-MM-dd");

                    Txt.AcionaTextoNoArquivo(path, conjuntos);

                    //Espera de 1 segundo
                    System.Threading.Thread.Sleep(1000);
                }
            }
        }
    }
}
