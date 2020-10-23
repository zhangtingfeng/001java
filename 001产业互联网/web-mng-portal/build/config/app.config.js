import webpackMerge from "webpack-merge"

import base from "./config.base"
import modules from "./config.modules"
import rules from "./config.rules"
import settings from "./config.settings"

let config = settings(webpackMerge({}, rules, modules, base))

export default config