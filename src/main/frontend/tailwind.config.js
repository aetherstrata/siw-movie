/** @type {import('tailwindcss').Config} */
import defaultTheme from "tailwindcss/defaultTheme"

module.exports = {
    content: ["../resources/templates/**/*.{html,js}"],
    theme: {
        extend: {
            width:{
                "1/10": "10%"
            },
            spacing:{
                112: "28rem",
                128: "32rem",
                256: "64rem"
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
