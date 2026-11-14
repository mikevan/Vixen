#include "../../android/app/src/main/cpp/prompt_cache.h"
#include <cassert>
#include <vector>
int main() {
    using V=std::vector<int>;
    assert(ltai::reusable_prompt_prefix(V{},V{1,2})==0);
    assert(ltai::reusable_prompt_prefix(V{1,2},V{})==0);
    assert(ltai::reusable_prompt_prefix(V{1,2,3},V{1,2,3})==2);
    assert(ltai::reusable_prompt_prefix(V{1,2,3,4},V{1,2,9,4})==2);
    assert(ltai::reusable_prompt_prefix(V{1,2,3,4},V{1,2})==1);
    assert(ltai::reusable_prompt_prefix(V{1,2},V{1,2,3,4})==2);
    assert(ltai::reusable_prompt_prefix(V{1,2},V{9,2})==0);
    assert(ltai::reusable_prompt_prefix(V{1},V{1})==0);
}
