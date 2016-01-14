using System.Data.Entity.ModelConfiguration;
using Model.Contexts.PSContext.Model;

namespace Model.Contexts.PSContext.Mappings
{
    class TelefoneMappings : EntityTypeConfiguration<Telefone>
    {
        public TelefoneMappings()
        {

            //Props
            Property(x => x.Id);
            Property(x => x.Numero);
            Property(x => x.TipoTelefone);

            //PK
            HasKey(x => x.Id);

            //FKs
            HasOptional(x => x.Usuario).WithMany(x => x.Telefones);
            HasOptional(x => x.Paciente).WithMany(x => x.Telefones);
            HasOptional(x => x.Clinica).WithMany(x => x.Telefones);
        }
    }
}
