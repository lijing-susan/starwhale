package ai.starwhale.mlops.api.protocol.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
public class UserRoleUpdateRequest {

    @NotNull
    @JsonProperty("currentUserPwd")
    private String currentUserPwd;

    @NotNull
    @JsonProperty("roleId")
    private String roleId;

}