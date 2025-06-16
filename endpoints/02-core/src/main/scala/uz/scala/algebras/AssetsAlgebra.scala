package uz.scala.algebras

import java.util.UUID

import cats.MonadThrow
import cats.implicits.toFlatMapOps
import cats.implicits.toFunctorOps

import uz.scala.aws.s3.S3Client
import uz.scala.domain.FileMeta
import uz.scala.effects.GenUUID

trait AssetsAlgebra[F[_]] {
  def create(meta: FileMeta[F]): F[UUID]
}
object AssetsAlgebra {
  def make[F[_]: MonadThrow: GenUUID: Lambda[M[_] => fs2.Compiler[M, M]]](
      s3Client: S3Client[F]
    ): AssetsAlgebra[F] =
    new AssetsAlgebra[F] {
      override def create(meta: FileMeta[F]): F[UUID] =
        for {
          id <- GenUUID[F].make
          key <- genFileKey(meta.fileName)
          _ <- meta.bytes.through(s3Client.putObject(key)).compile.drain
        } yield id

      private def getFileType(filename: String): String = {
        val extension = filename.substring(filename.lastIndexOf('.') + 1)
        extension.toLowerCase
      }

      private def genFileKey(orgFilename: String): F[String] =
        GenUUID[F].make.map { uuid =>
          uuid.toString + "." + getFileType(orgFilename)
        }
    }
}
