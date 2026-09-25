package models.update_user;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UpdateUserPatchBodyModel(
        String username,
        String firstName,
        String lastName,
        String email
) {

    public static UpdateUserPatchBodyModel withUsername(String value)   {
        return new UpdateUserPatchBodyModel(value, null, null, null); }

    public static UpdateUserPatchBodyModel withFirstName(String value)  {
        return new UpdateUserPatchBodyModel(null, value, null, null); }

    public static UpdateUserPatchBodyModel withLastName(String value)   {
        return new UpdateUserPatchBodyModel(null, null, value, null); }

    public static UpdateUserPatchBodyModel withEmail(String value)      {
        return new UpdateUserPatchBodyModel(null, null, null, value); }
}