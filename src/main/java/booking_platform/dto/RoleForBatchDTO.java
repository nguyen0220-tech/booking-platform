package booking_platform.dto;

import booking_platform.enumdef.RoleName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RoleForBatchDTO {
    private Long userId;
    private RoleName name;
}
