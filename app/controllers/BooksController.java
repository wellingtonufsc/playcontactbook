package controllers;
import controllers.Auth.Secured;
import models.Book;
import models.Tag;
import models.User;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

/**
 * Created by azeem on 4/30/2017.
 */

import play.mvc.Security;
import views.html.books.*;
import views.html.errors.*;

import javax.inject.Inject;

public class BooksController  extends Controller{

    @Inject
    FormFactory formFactory;

    // for all books

    public Result index(){
        List<Book> books = Book.find.all();
        return ok(index.render(books,"Contatos cadastrados"));
    }

    public Result show(Integer id){
        Book book = Book.find.byId(id);
        if(book==null){
            return notFound(_404.render());
        }
        return ok(show.render(book));
    }

    @Security.Authenticated(Secured.class)
    public Result create(){
        List<User> authors = User.find.all();
        Form<Book> bookForm = formFactory.form(Book.class);
        List<Tag> tags = Tag.find.all();
        return ok(create.render(bookForm,authors,tags));
    }

    @Security.Authenticated(Secured.class)
    public Result save() {

        Form<Book> bookForm = formFactory.form(Book.class).bindFromRequest();
        List<User> authors = User.find.all();
        List<Tag> tags = Tag.find.all();

        if(bookForm.hasErrors()){
            flash("danger","Input validation failed.");
            return badRequest(create.render(bookForm,authors,tags));
        }

        // handle book photo upload

        Http.MultipartFormData<File> body = request().body().asMultipartFormData();

        Http.MultipartFormData.FilePart<File> picture = body.getFile("photo");



        if (picture == null) {
            flash("danger", "Book photo is missing.");
            return badRequest(create.render(bookForm,authors,tags));
        }

        String contentType = picture.getContentType();


        if((!contentType.contains("image")) ){
            flash("danger", "Image is required.");
            return badRequest(create.render(bookForm,authors,tags));
        }

        String uploadFileName = this.saveFile(picture);

        if(uploadFileName == null){
            flash("danger", "Unable to upload file to uploads dir, please try again.");
            return badRequest(create.render(bookForm,authors,tags));
        }

        Map<String,String> input = bookForm.rawData();

        String authorEmail = input.get("author_email");

        Book book = bookForm.get();
        book.photo = uploadFileName;
        book.author = User.find.byId(authorEmail);

        book.tags = Book.parseTagsFromRequest(input);

        book.save();

        flash("success","Contato cadastrado!");

        return redirect(routes.BooksController.index());
    }

    @Security.Authenticated(Secured.class)
    public Result edit(Integer id){

        Book book = Book.find.byId(id);
        if(book==null){
            return notFound(_404.render());
        }

        List<User> authors = User.find.all();
        List<Tag> tags = Tag.find.all();

        Form<Book> bookForm = formFactory.form(Book.class).fill(book);
        return ok(edit.render(bookForm,authors,tags));
    }

    @Security.Authenticated(Secured.class)
    public Result update(Integer id){

        Form<Book> bookForm = formFactory.form(Book.class).bindFromRequest();
        List<User> authors = User.find.all();


        if (bookForm.hasErrors()){
            List<Tag> tags = Tag.find.all();
            flash("danger","Input Validation failed.");
            return badRequest(edit.render(bookForm,authors,tags));
        }

        Book book = bookForm.get();
        Book oldBook = Book.find.byId(id);

        if(oldBook == null){
            flash("danger","Book Not Found");
            return redirect(routes.BooksController.edit(id));
        }

        Http.MultipartFormData<File> body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart<File> picture = body.getFile("photo");
        String contentType = picture.getContentType();

        if (!contentType.equals("application/octet-stream")) {
            if(!contentType.contains("image")){
                flash("danger", "Image is required.");
                return redirect(routes.BooksController.edit(id));
            }
            String uploadFileName = this.saveFile(picture);
            if(uploadFileName == null){
                flash("danger", "Unable to upload file to uploads dir, please try again.");
                return redirect(routes.BooksController.edit(id));
            }
            new File("public/uploads/"+oldBook.photo).delete();
            oldBook.photo = uploadFileName;
        }

        Map<String,String> input = bookForm.rawData();
        String authorEmail = input.get("author_email");

        oldBook.name = book.name;
        oldBook.author = User.find.byId(authorEmail);
        oldBook.phone = book.phone;
        oldBook.email = book.email;
        oldBook.age = book.age;
        oldBook.birthdate = book.birthdate;
        oldBook.tags = Book.parseTagsFromRequest(input);
        oldBook.update();

        flash("success","Contato atualizado!");

        return redirect(routes.BooksController.edit(id));
    }

    @Security.Authenticated(Secured.class)
    public Result destroy(Integer id){

        Book book = Book.find.byId(id);
        if(book == null){
            flash("danger","Book Not Found");
            return notFound();
        }

        new File("public/uploads/"+book.photo).delete();

        book.delete();
        flash("success","Book Deleted Successfully");

        return ok();
    }

    private String saveFile(Http.MultipartFormData.FilePart<File> picture){

        File photo = picture.getFile();
        String fileName = picture.getFilename();
        long unixTime = System.currentTimeMillis() / 1000L;
        String uploadFileName = unixTime+"_"+fileName;
        File uploadDir = new File("public/uploads/"+uploadFileName);

        try {
            Files.copy(photo.toPath(), uploadDir.toPath());
        } catch (IOException e) {
            return null;
        }


        return uploadFileName;
    }

}
