using System.Data.Entity;
using Model.Model;

namespace Model.Contexts
{
    class PsContext : DbContext
    {
        //Aqui será 
        public DbSet<Usuario> Usuarios { get; set; }
        public DbSet<Telefone> Telefones { get; set; }

        protected override void OnModelCreating(DbModelBuilder modelBuilder)
        {
            //Separar por item futuramente



            //Usuarios
            //Usuarios
            modelBuilder.Entity<Usuario>().HasKey(m => m.Id);

            //Não é necessário, está aqui apenas pra listarmos
            modelBuilder.Entity<Usuario>().Property(m => m.Id);
            modelBuilder.Entity<Usuario>().Property(m => m.Nome);




            //Telefone
            //Telefone
            //Key
            modelBuilder.Entity<Telefone>().HasKey(m => m.Id);

            //Mapeamento FK
            modelBuilder.Entity<Telefone>().HasRequired(m => m.Usuario).WithMany(m => m.Telefones).HasForeignKey(m => m.UsuarioId).WillCascadeOnDelete(false);

            //Não é necessário listarmos se não tiver nenhuma opção a mais
            //modelBuilder.Entity<Telefone>().Property(m => m.Id);
            modelBuilder.Entity<Telefone>().Property(m => m.Numero).HasMaxLength(50);
            modelBuilder.Entity<Telefone>().Property(m => m.UsuarioId).IsRequired();
        }
    }
}