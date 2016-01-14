using System.Collections.Generic;

namespace Model.Contexts.PSContext.Model
{
    class Clinica
    {
        public Clinica()
        {
            Telefones = new HashSet<Telefone>();
            Enderecos = new HashSet<Endereco>();
        }

        //Props simples
        public int    Id { get; set; }
        public string Nome { get; set; }
        public string Email { get; set; }
        public string Cnes { get; set; }

        //Fks

        //Props complexas
        public virtual ICollection<Telefone> Telefones { get; set; }
        public virtual ICollection<Endereco> Enderecos { get; set; }
    }
}
