package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.ebean.Finder;
import io.ebean.Model;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by azeem on 4/30/2017.
 */

@Entity
@Table(name = "books")
public class Book extends Model {

    @Id
    @GeneratedValue
    public Integer id;

    @Constraints.Required
    public String name;

    @Constraints.Required
    public Integer phone;

    public String email;

    public Integer age;

    public String birthdate;

    public String photo;


    @ManyToOne(optional=false,fetch = FetchType.LAZY)
    public User author;

    @ManyToMany
    public List<Tag> tags;

    public static Finder<Integer,Book> find = new Finder<>(Book.class);

    public static List<Tag> parseTagsFromRequest(Map<String,String> input){

        List<Tag> bookTags = new ArrayList<>();

        for(Map.Entry<String, String> entry : input.entrySet()){
            if(entry.getKey().contains("tagIds")){
                bookTags.add(new Tag(Integer.valueOf(entry.getValue())));
            }
        }

        return bookTags;
    }
}
