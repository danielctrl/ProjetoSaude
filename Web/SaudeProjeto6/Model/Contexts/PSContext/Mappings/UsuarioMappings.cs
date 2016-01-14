using System.Data.Entity.ModelConfiguration;
using Model.Contexts.PSContext.Model;

namespace Model.Contexts.PSContext.Mappings
{
    class UsuarioMappings : EntityTypeConfiguration<Usuario>
    {
        public UsuarioMappings()
        {
            //Props
            Property(x => x.Id);
            Property(x => x.Nome);
            Property(x => x.Senha);
            Property(x => x.Email);
            Property(x => x.Conselho);
            Property(x => x.TipoUsuario);

            //PK
            this.HasKey(m => m.Id);

            //FKs
        }
    }
}
