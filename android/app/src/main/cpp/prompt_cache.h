#pragma once
#include <algorithm>
#include <cstddef>
#include <vector>
namespace ltai {
// Re-evaluate the final prompt token so sampling never reads logits from an old answer.
template<class Token> std::size_t reusable_prompt_prefix(const std::vector<Token>& previous,const std::vector<Token>& next) {
    if(next.empty())return 0;
    const auto limit=std::min(previous.size(),next.size()-1);
    std::size_t n=0;while(n<limit && previous[n]==next[n])++n;return n;
}
}
