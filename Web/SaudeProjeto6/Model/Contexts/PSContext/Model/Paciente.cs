using System;
using System.Collections.Generic;

namespace Model.Contexts.PSContext.Model
{
    class Paciente
    {
        public Paciente()
        {
            Telefones = new HashSet<Telefone>();
            Enderecos = new HashSet<Endereco>();
            FamiliaresA = new HashSet<PacienteParentesco>();
            FamiliaresB = new HashSet<PacienteParentesco>();
            Convenios = new HashSet<Convenio>();
        }

        //Props simples
        public int Id { get; set; }
        public int Codigo { get; set; }
        public string Nome { get; set; }
        public DateTime Nascimento { get; set; }
        public int Sexo { get; set; }
        public string Email { get; set; }
        public string Observacao { get; set; }
        public string NaturalidadeCidade { get; set; }
        public string NaturalidadeUf { get; set; }
        public string NaturalidadePais { get; set; }
        public string Nacionalidade { get; set; }
        public string Etnia { get; set; }
        public string Cpf { get; set; }
        public string Rg { get; set; }
        public int? Religiao { get; set; }
        public string EstadoCivel { get; set; }
        public string Profissao { get; set; }
        public int Escolaridade { get; set; }
        public bool Obito { get; set; }
        public bool Ativo { get; set; }
        public string Cns { get; set; }
        
        //Fks

        //Props complexas
        public virtual ICollection<Telefone> Telefones { get; set; }
        public virtual ICollection<Endereco> Enderecos { get; set; }
        public virtual ICollection<PacienteParentesco> FamiliaresA { get; set; }
        public virtual ICollection<PacienteParentesco> FamiliaresB { get; set; }
        public virtual ICollection<Convenio> Convenios { get; set; }
    }
}