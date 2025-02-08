package com.nivuskorea.mealticketmanagement.validation;

import jakarta.validation.GroupSequence;
import jakarta.validation.groups.Default;

@GroupSequence({ValidationGroups.NotBlankGroup.class, ValidationGroups.PatternGroup.class})
public interface ValidationSequence {
}
