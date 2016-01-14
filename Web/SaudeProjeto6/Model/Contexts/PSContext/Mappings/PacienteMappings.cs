using System.Data.Entity.ModelConfiguration;
using Model.Contexts.PSContext.Model;

namespace Model.Contexts.PSContext.Mappings
{
    class PacienteMappings : EntityTypeConfiguration<Paciente>
    {
        public PacienteMappings()
        {

            //Props
            Property(x => x.Id);
            Property(x => x.Codigo);
            Property(x => x.Nome);
            Property(x => x.Nascimento);
            Property(x => x.Sexo);
            Property(x => x.Email);
            Property(x => x.Observacao);
            Property(x => x.NaturalidadeCidade);
            Property(x => x.NaturalidadeUf);
            Property(x => x.NaturalidadePais);
            Property(x => x.Nacionalidade);
            Property(x => x.Etnia);
            Property(x => x.Cpf);
            Property(x => x.Rg);
            Property(x => x.Religiao);
            Property(x => x.EstadoCivel);
            Property(x => x.Profissao);
            Property(x => x.Escolaridade);
            Property(x => x.Obito);
            Property(x => x.Ativo);
            Property(x => x.Cns);

            //PK
            HasKey(x => x.Id);

            //FKs
        }
    }
}
