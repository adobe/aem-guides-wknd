// .storybook/main.js

module.exports = {
    stories: ['../stories/*.stories.@(js|mdx)'],
    core: {
        builder: 'webpack5',
      },
  }