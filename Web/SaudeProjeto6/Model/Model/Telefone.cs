using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Model.Model
{
    class Telefone
    {
        //Props complexas
        public Usuario Usuario { get; set; }
        
        //Props simples
        public int Id { get; set; }
        public int UsuarioId { get; set; }
        public string Numero { get; set; }
    }
}
