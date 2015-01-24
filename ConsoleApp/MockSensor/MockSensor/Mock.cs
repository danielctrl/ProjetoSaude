using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using MockSensor.Model;

namespace MockSensor
{
    static class Mock
    {
        private static Random rdn = new Random();

        public static List<Conjunto> PopulaConjuntosDefinivel(int[] qtdDeSensoresPorConjunto, double tempInicial)
        {

            var conjuntos = new List<Conjunto>();

            for (int i = 0; i < qtdDeSensoresPorConjunto.Length; i++)
            {
                var conjunto = new Conjunto() { id = i.ToString() };

                for (int j = 0; j < qtdDeSensoresPorConjunto[i]; j++)
                {
                    conjunto.sensores.Add(new Sensor() { id = j.ToString(), temp = tempInicial });
                }

                conjuntos.Add(conjunto);
            }

            return conjuntos;
        }

        public static void TempVariacaoDefinivel(Sensor sensor, double variacao)
        {
            double tempMax, tempMin;

            if (rdn.Next(1) == 0)
            {
                tempMax = sensor.temp;
                tempMin = sensor.temp - variacao;
            }
            else
            {
                tempMax = sensor.temp + variacao;
                tempMin = sensor.temp;
            }

            sensor.temp = rdn.NextDouble() * (tempMax - tempMin) + tempMin;
        }
    }
}
