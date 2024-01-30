package com.manageyourbrain.domain;

import static com.manageyourbrain.domain.MemoryTestSamples.*;
import static com.manageyourbrain.domain.TagTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.manageyourbrain.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class TagTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tag.class);
        Tag tag1 = getTagSample1();
        Tag tag2 = new Tag();
        assertThat(tag1).isNotEqualTo(tag2);

        tag2.setId(tag1.getId());
        assertThat(tag1).isEqualTo(tag2);

        tag2 = getTagSample2();
        assertThat(tag1).isNotEqualTo(tag2);
    }

    @Test
    void memoryTest() throws Exception {
        Tag tag = getTagRandomSampleGenerator();
        Memory memoryBack = getMemoryRandomSampleGenerator();

        tag.addMemory(memoryBack);
        assertThat(tag.getMemories()).containsOnly(memoryBack);
        assertThat(memoryBack.getTag()).isEqualTo(tag);

        tag.removeMemory(memoryBack);
        assertThat(tag.getMemories()).doesNotContain(memoryBack);
        assertThat(memoryBack.getTag()).isNull();

        tag.memories(new HashSet<>(Set.of(memoryBack)));
        assertThat(tag.getMemories()).containsOnly(memoryBack);
        assertThat(memoryBack.getTag()).isEqualTo(tag);

        tag.setMemories(new HashSet<>());
        assertThat(tag.getMemories()).doesNotContain(memoryBack);
        assertThat(memoryBack.getTag()).isNull();
    }
}
