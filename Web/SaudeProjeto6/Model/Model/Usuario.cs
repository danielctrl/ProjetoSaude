using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Model.Model
{
    class Usuario
    {
        public Usuario()
        {
            //Talvez seja necessário inicializar a lista
            //Telefones = new ???
        }

        public virtual ICollection<Telefone> Telefones { get; set; }

        public int Id { get; set; }
        public string Nome { get; set; }
    }
}
