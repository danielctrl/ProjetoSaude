using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MockSensor.Model
{
    public class Conjunto
    {
        public string id;
        public List<Sensor> sensores = new List<Sensor>();

        public static void TrocaTempGeral(List<Conjunto> conjuntos, double variacao)
        {
            conjuntos.ForEach(x =>
                x.sensores.ForEach(y => Mock.TempVariacaoDefinivel(y, variacao))
            );
        }
    }
}
