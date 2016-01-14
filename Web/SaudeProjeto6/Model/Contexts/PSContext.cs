using System.Data.Entity;
using System.Data.Entity.ModelConfiguration.Conventions;
using Model.Contexts.PSContext.Mappings;
using Model.Contexts.PSContext.Model;

namespace Model.Contexts
{
    class PsContext : DbContext
    {
        //Aqui será 
        public DbSet<Usuario> Usuarios { get; set; }
        public DbSet<Telefone> Telefones { get; set; }

        protected override void OnModelCreating(DbModelBuilder modelBuilder)
        {
            modelBuilder.Conventions.Remove<PluralizingTableNameConvention>();

            modelBuilder.Configurations.Add(new ClinicaMappings());
            modelBuilder.Configurations.Add(new ConvenioMappings());
            modelBuilder.Configurations.Add(new EnderecoMappings());
            modelBuilder.Configurations.Add(new PacienteMappings());
            modelBuilder.Configurations.Add(new TelefoneMappings());
            modelBuilder.Configurations.Add(new UsuarioMappings());
        }
    }
}