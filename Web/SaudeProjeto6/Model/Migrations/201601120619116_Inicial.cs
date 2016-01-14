namespace Model.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class Inicial : DbMigration
    {
        public override void Up()
        {
            CreateTable(
                "dbo.Telefone",
                c => new
                    {
                        Id = c.Int(nullable: false, identity: true),
                        Numero = c.String(),
                        TipoTelefone = c.Int(nullable: false),
                        Clinica_Id = c.Int(),
                        Paciente_Id = c.Int(),
                        Usuario_Id = c.Int(),
                    })
                .PrimaryKey(t => t.Id)
                .ForeignKey("dbo.Clinica", t => t.Clinica_Id)
                .ForeignKey("dbo.Paciente", t => t.Paciente_Id)
                .ForeignKey("dbo.Usuario", t => t.Usuario_Id)
                .Index(t => t.Clinica_Id)
                .Index(t => t.Paciente_Id)
                .Index(t => t.Usuario_Id);
            
            CreateTable(
                "dbo.Clinica",
                c => new
                    {
                        Id = c.Int(nullable: false, identity: true),
                        Nome = c.String(),
                        Email = c.String(),
                        Cnes = c.String(),
                    })
                .PrimaryKey(t => t.Id);
            
            CreateTable(
                "dbo.Endereco",
                c => new
                    {
                        Id = c.Int(nullable: false, identity: true),
                        Cep = c.String(),
                        Estado = c.String(),
                        Cidade = c.String(),
                        Bairro = c.String(),
                        Logradouro = c.String(),
                        Numero = c.String(),
                        Complemento = c.String(),
                        Clinica_Id = c.Int(),
                        Paciente_Id = c.Int(),
                        Usuario_Id = c.Int(),
                    })
                .PrimaryKey(t => t.Id)
                .ForeignKey("dbo.Clinica", t => t.Clinica_Id)
                .ForeignKey("dbo.Paciente", t => t.Paciente_Id)
                .ForeignKey("dbo.Usuario", t => t.Usuario_Id)
                .Index(t => t.Clinica_Id)
                .Index(t => t.Paciente_Id)
                .Index(t => t.Usuario_Id);
            
            CreateTable(
                "dbo.Paciente",
                c => new
                    {
                        Id = c.Int(nullable: false, identity: true),
                        Codigo = c.Int(nullable: false),
                        Nome = c.String(),
                        Nascimento = c.DateTime(nullable: false),
                        Sexo = c.Int(nullable: false),
                        Email = c.String(),
                        Observacao = c.String(),
                        NaturalidadeCidade = c.String(),
                        NaturalidadeUf = c.String(),
                        NaturalidadePais = c.String(),
                        Nacionalidade = c.String(),
                        Etnia = c.String(),
                        Cpf = c.String(),
                        Rg = c.String(),
                        Religiao = c.Int(),
                        EstadoCivel = c.String(),
                        Profissao = c.String(),
                        Escolaridade = c.Int(nullable: false),
                        Obito = c.Boolean(nullable: false),
                        Ativo = c.Boolean(nullable: false),
                        Cns = c.String(),
                    })
                .PrimaryKey(t => t.Id);
            
            CreateTable(
                "dbo.Convenio",
                c => new
                    {
                        Id = c.Int(nullable: false, identity: true),
                        Nome = c.String(),
                        Registro = c.String(),
                    })
                .PrimaryKey(t => t.Id);
            
            CreateTable(
                "dbo.PacienteParentesco",
                c => new
                    {
                        Id = c.Int(nullable: false, identity: true),
                        TipoParentescoAehDeB = c.Int(nullable: false),
                        PacientesFamiliarA_Id = c.Int(),
                        PacientesFamiliarB_Id = c.Int(),
                        Paciente_Id = c.Int(),
                        Paciente_Id1 = c.Int(),
                    })
                .PrimaryKey(t => t.Id)
                .ForeignKey("dbo.Paciente", t => t.PacientesFamiliarA_Id)
                .ForeignKey("dbo.Paciente", t => t.PacientesFamiliarB_Id)
                .ForeignKey("dbo.Paciente", t => t.Paciente_Id)
                .ForeignKey("dbo.Paciente", t => t.Paciente_Id1)
                .Index(t => t.PacientesFamiliarA_Id)
                .Index(t => t.PacientesFamiliarB_Id)
                .Index(t => t.Paciente_Id)
                .Index(t => t.Paciente_Id1);
            
            CreateTable(
                "dbo.Usuario",
                c => new
                    {
                        Id = c.Int(nullable: false, identity: true),
                        Nome = c.String(),
                        Senha = c.String(),
                        Email = c.String(),
                        Conselho = c.String(),
                        TipoUsuario = c.Int(nullable: false),
                    })
                .PrimaryKey(t => t.Id);
            
            CreateTable(
                "dbo.ConvenioPaciente",
                c => new
                    {
                        Convenio_Id = c.Int(nullable: false),
                        Paciente_Id = c.Int(nullable: false),
                    })
                .PrimaryKey(t => new { t.Convenio_Id, t.Paciente_Id })
                .ForeignKey("dbo.Convenio", t => t.Convenio_Id, cascadeDelete: true)
                .ForeignKey("dbo.Paciente", t => t.Paciente_Id, cascadeDelete: true)
                .Index(t => t.Convenio_Id)
                .Index(t => t.Paciente_Id);
            
        }
        
        public override void Down()
        {
            DropForeignKey("dbo.Telefone", "Usuario_Id", "dbo.Usuario");
            DropForeignKey("dbo.Telefone", "Paciente_Id", "dbo.Paciente");
            DropForeignKey("dbo.Telefone", "Clinica_Id", "dbo.Clinica");
            DropForeignKey("dbo.Endereco", "Usuario_Id", "dbo.Usuario");
            DropForeignKey("dbo.Endereco", "Paciente_Id", "dbo.Paciente");
            DropForeignKey("dbo.PacienteParentesco", "Paciente_Id1", "dbo.Paciente");
            DropForeignKey("dbo.PacienteParentesco", "Paciente_Id", "dbo.Paciente");
            DropForeignKey("dbo.PacienteParentesco", "PacientesFamiliarB_Id", "dbo.Paciente");
            DropForeignKey("dbo.PacienteParentesco", "PacientesFamiliarA_Id", "dbo.Paciente");
            DropForeignKey("dbo.ConvenioPaciente", "Paciente_Id", "dbo.Paciente");
            DropForeignKey("dbo.ConvenioPaciente", "Convenio_Id", "dbo.Convenio");
            DropForeignKey("dbo.Endereco", "Clinica_Id", "dbo.Clinica");
            DropIndex("dbo.ConvenioPaciente", new[] { "Paciente_Id" });
            DropIndex("dbo.ConvenioPaciente", new[] { "Convenio_Id" });
            DropIndex("dbo.PacienteParentesco", new[] { "Paciente_Id1" });
            DropIndex("dbo.PacienteParentesco", new[] { "Paciente_Id" });
            DropIndex("dbo.PacienteParentesco", new[] { "PacientesFamiliarB_Id" });
            DropIndex("dbo.PacienteParentesco", new[] { "PacientesFamiliarA_Id" });
            DropIndex("dbo.Endereco", new[] { "Usuario_Id" });
            DropIndex("dbo.Endereco", new[] { "Paciente_Id" });
            DropIndex("dbo.Endereco", new[] { "Clinica_Id" });
            DropIndex("dbo.Telefone", new[] { "Usuario_Id" });
            DropIndex("dbo.Telefone", new[] { "Paciente_Id" });
            DropIndex("dbo.Telefone", new[] { "Clinica_Id" });
            DropTable("dbo.ConvenioPaciente");
            DropTable("dbo.Usuario");
            DropTable("dbo.PacienteParentesco");
            DropTable("dbo.Convenio");
            DropTable("dbo.Paciente");
            DropTable("dbo.Endereco");
            DropTable("dbo.Clinica");
            DropTable("dbo.Telefone");
        }
    }
}
