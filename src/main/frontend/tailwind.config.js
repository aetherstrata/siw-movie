/** @type {import('tailwindcss').Config} */
import defaultTheme from "tailwindcss/defaultTheme"

module.exports = {
    content: ["../resources/templates/**/*.{html,js}"],
    theme: {
        extend: {
            spacing:{
                112: "28rem",
                128: "32rem"
            },
            fontFamily: {
                comfortaa: ["Comfortaa", ...defaultTheme.fontFamily.sans]
            }
        },
    },
    plugins: [
        require('@tailwindcss/forms')
    ],
}
